import React, { useCallback, useEffect, useState } from 'react'
import { useHistory, useParams } from 'react-router-dom'
import { createForm } from '@/components/Form'
import useTranslation from '@/hooks/useTranslation'
import { isModified } from '@/utils'
import Divider from '@/components/Divider'
import _ from 'lodash'
import ResourcePoolSelector from '@/domain/setting/components/ResourcePoolSelector'
import { IModelVersionSchema, StepSpec } from '@/domain/model/schemas/modelVersion'
import Editor from '@monaco-editor/react'
import yaml from 'js-yaml'
import { createUseStyles } from 'react-jss'
import { toaster } from 'baseui/toast'
import Button from '@starwhale/ui/Button'
import { ICreateJobFormSchema, ICreateJobSchema, IJobFormSchema } from '../schemas/job'
import { FormSelect, Toggle } from '@starwhale/ui/Select'
import DatasetTreeSelector from '@/domain/dataset/components/DatasetTreeSelector'
import RuntimeTreeSelector from '@runtime/components/RuntimeTreeSelector'
import ModelTreeSelector from '@/domain/model/components/ModelTreeSelector'
import { IModelTreeSchema } from '@/domain/model/schemas/model'

const { Form, FormItem, useForm, FormItemLabel } = createForm<ICreateJobFormSchema>()

const useStyles = createUseStyles({
    row3: {
        display: 'grid',
        gap: 40,
        gridTemplateColumns: '280px 300px 280px',
        gridTemplateRows: 'minmax(0px, max-content)',
    },
    row4: {
        display: 'grid',
        columnGap: 40,
        gridTemplateColumns: '120px 120px 480px 100px',
    },
    rowModel: {
        display: 'grid',
        columnGap: 40,
        gridTemplateColumns: '660px 280px 280px',
        gridTemplateRows: 'minmax(0px, max-content)',
    },
    resource: {
        gridColumnStart: 3,
        display: 'grid',
        columnGap: 40,
        gridTemplateColumns: '260px 120px 40px',
    },
})

export interface IJobFormProps {
    job?: IJobFormSchema
    onSubmit: (data: ICreateJobSchema) => Promise<void>
}

const RuntimeType = {
    BUILTIN: 'builtIn',
    OTHER: 'other',
}

export default function JobForm({ job, onSubmit }: IJobFormProps) {
    const styles = useStyles()
    const [values, setValues] = useState<ICreateJobFormSchema | undefined>(undefined)
    const { projectId } = useParams<{ projectId: string }>()
    const [modelTree, setModelTree] = useState<IModelTreeSchema[]>([])
    const [modelVersionId, setModelVersionId] = useState('')
    const [modelVersionHandler, setModelVersionHandler] = useState('')
    const [rawType, setRawType] = React.useState(false)
    const [stepSpecOverWrites, setStepSpecOverWrites] = React.useState('')
    const [t] = useTranslation()
    // const [resourcePool, setResourcePool] = React.useState<ISystemResourcePool | undefined>()

    const [form] = useForm()
    const history = useHistory()

    const [loading, setLoading] = useState(false)

    const [builtInRuntime, setBuiltInRuntime] = useState<string>('')
    const [type, setType] = useState(builtInRuntime ? RuntimeType.BUILTIN : '')

    const handleValuesChange = useCallback(
        (_changes, values_) => {
            if ('modelVersionUrl' in _changes) {
                setModelVersionHandler('')
            }
            if (values_.modelVersionUrl) {
                setModelVersionId(values_.modelVersionUrl[0])
            }
            if (values_.modelVersionHandler) {
                setModelVersionHandler(values_.modelVersionHandler)
            }
            let rawTypeTmp = values_.rawType
            if ('rawType' in _changes && !_changes.rawType) {
                try {
                    yaml.load(stepSpecOverWrites)
                    rawTypeTmp = false
                } catch (e) {
                    toaster.negative(t('wrong yaml syntax'), { autoHideDuration: 1000 })
                    form.setFieldsValue({
                        rawType: true,
                    })
                    rawTypeTmp = true
                }
            }
            setRawType(rawTypeTmp)
            setValues({
                ...values_,
                rawType: rawTypeTmp,
            })
        },
        [stepSpecOverWrites, form, t]
    )

    const modelVersion: IModelVersionSchema | undefined = React.useMemo(() => {
        if (!modelTree || !modelVersionId) return undefined
        let version: IModelVersionSchema | undefined
        modelTree?.forEach((v) =>
            v.versions.forEach((versionTmp) => {
                if (versionTmp.id === modelVersionId) {
                    version = versionTmp
                }
            })
        )
        return version
    }, [modelTree, modelVersionId])

    useEffect(() => {
        if (!modelVersion) return
        setBuiltInRuntime(modelVersion?.builtInRuntime ?? '')
        setType(modelVersion?.builtInRuntime ? RuntimeType.BUILTIN : RuntimeType.OTHER)
    }, [modelVersion])

    const fullStepSource: StepSpec[] | undefined = React.useMemo(() => {
        if (!modelVersion) return undefined
        return modelVersion?.stepSpecs ?? []
    }, [modelVersion])

    const stepSource: StepSpec[] | undefined = React.useMemo(() => {
        if (!fullStepSource) return undefined
        if (stepSpecOverWrites) {
            try {
                return (yaml.load(stepSpecOverWrites) ?? []) as StepSpec[]
            } catch (e) {
                return []
            }
        }
        return fullStepSource.filter((v: StepSpec) => v?.job_name === modelVersionHandler)
    }, [fullStepSource, modelVersionHandler, stepSpecOverWrites])

    const handleModelHandlerChange = useCallback(
        (value) => {
            setModelVersionHandler(value)
            setStepSpecOverWrites(yaml.dump(fullStepSource?.filter((v: StepSpec) => v?.job_name === value)))
        },
        [setModelVersionHandler, fullStepSource]
    )

    const checkStepSource = useCallback(
        (value) => {
            try {
                yaml.load(value)
            } catch (e) {
                toaster.negative(t('wrong yaml syntax'), { autoHideDuration: 1000, key: 'yaml' })
                return false
            }
            return true
        },
        [t]
    )

    const handleFinish = useCallback(
        async (values_: ICreateJobFormSchema) => {
            setLoading(true)
            if (values_.rawType && !checkStepSource(stepSpecOverWrites)) return
            try {
                await onSubmit({
                    ..._.omit(values_, [
                        'modelId',
                        'datasetId',
                        'datasetVersionId',
                        'datasetVersionIdsArr',
                        'runtimeId',
                        'runtimeVersionUrl',
                        'rawType',
                        'stepSpecOverWrites',
                        'modelVersionHandler',
                        'modelVersionUrl',
                    ]),
                    runtimeVersionUrl: type === RuntimeType.BUILTIN ? '' : values_.runtimeVersionUrl[0],
                    modelVersionUrl: values_.modelVersionUrl[0],
                    datasetVersionUrls: values_.datasetVersionIdsArr?.join(','),
                    stepSpecOverWrites: values_.rawType ? stepSpecOverWrites : yaml.dump(stepSource),
                })
                history.goBack()
            } finally {
                setLoading(false)
            }
        },
        [onSubmit, history, stepSpecOverWrites, stepSource, checkStepSource, builtInRuntime, type]
    )

    const handleEditorChange = React.useCallback(
        (value: string) => {
            setStepSpecOverWrites(value)
        },
        [setStepSpecOverWrites]
    )

    return (
        <Form form={form} initialValues={values} onFinish={handleFinish} onValuesChange={handleValuesChange}>
            <Divider orientation='top'>{t('Environment')}</Divider>
            <div className={styles.row3}>
                <FormItem label={t('Resource Pool')} name='resourcePool' required>
                    <ResourcePoolSelector autoSelected />
                </FormItem>
                <FormItem label={t('eval debug mode')} name='debugMode'>
                    <Toggle />
                </FormItem>
            </div>
            <Divider orientation='top'>{t('Model Information')}</Divider>
            <div className={styles.rowModel}>
                <FormItem label={t('Version')} required name='modelVersionUrl'>
                    <ModelTreeSelector projectId={projectId} onDataChange={setModelTree} />
                </FormItem>
                {modelVersionId && (
                    <FormItemLabel label={t('model.handler')}>
                        <div style={{ marginTop: '8px' }} />
                        <FormSelect
                            clearable={false}
                            value={modelVersionHandler}
                            onChange={handleModelHandlerChange}
                            options={
                                Array.from(new Set(fullStepSource?.map((tmp) => tmp.job_name))).map((tmp) => {
                                    return {
                                        label: tmp,
                                        id: tmp,
                                    }
                                }) ?? []
                            }
                        />
                    </FormItemLabel>
                )}
                <FormItem label={t('Raw Type')} name='rawType'>
                    <Toggle />
                </FormItem>
            </div>
            <div style={{ paddingBottom: '16px' }}>
                {stepSource &&
                    stepSource?.length > 0 &&
                    !rawType &&
                    stepSource?.map((spec, i) => {
                        return (
                            <div key={[spec?.name, i].join('')}>
                                <div
                                    style={{
                                        display: 'flex',
                                        minWidth: '280px',
                                        lineHeight: '1',
                                        alignItems: 'stretch',
                                        gap: '20px',
                                        marginBottom: '10px',
                                    }}
                                >
                                    <div
                                        style={{
                                            padding: '5px 20px',
                                            minWidth: '280px',
                                            background: '#EEF1F6',
                                            borderRadius: '4px',
                                        }}
                                    >
                                        <span style={{ color: 'rgba(2,16,43,0.60)' }}>{t('Step')}:&nbsp;</span>
                                        <span>{spec?.name}</span>
                                        <div style={{ marginTop: '3px' }} />
                                        <span style={{ color: 'rgba(2,16,43,0.60)' }}>{t('Task Amount')}:&nbsp;</span>
                                        <span>{spec?.replicas}</span>
                                    </div>
                                    {spec.resources &&
                                        spec.resources?.length > 0 &&
                                        spec.resources?.map((resource, j) => (
                                            <div
                                                key={j}
                                                style={{
                                                    padding: '5px 20px',
                                                    borderRadius: '4px',
                                                    border: '1px solid #E2E7F0',
                                                    // display: 'flex',
                                                    alignItems: 'center',
                                                }}
                                            >
                                                <span style={{ color: 'rgba(2,16,43,0.60)' }}>
                                                    {t('Resource')}:&nbsp;
                                                </span>
                                                <span> {resource?.type}</span>
                                                <div style={{ marginTop: '3px' }} />
                                                <span style={{ color: 'rgba(2,16,43,0.60)' }}>
                                                    {t('Resource Amount')}:&nbsp;
                                                </span>
                                                <span>{resource?.request}</span>
                                                <br />
                                            </div>
                                        ))}
                                </div>
                            </div>
                        )
                    })}
                <div
                    style={{
                        display: rawType ? 'block' : 'none',
                    }}
                >
                    <Editor
                        height='500px'
                        width='960px'
                        defaultLanguage='yaml'
                        value={stepSpecOverWrites}
                        theme='vs-dark'
                        // @ts-ignore
                        onChange={handleEditorChange}
                    />
                </div>
            </div>
            <Divider orientation='top'>{t('Datasets')}</Divider>
            <div className='bfc' style={{ width: '660px', marginBottom: '36px' }}>
                <FormItem label={t('Dataset Version')} name='datasetVersionIdsArr' required>
                    <DatasetTreeSelector projectId={projectId} />
                </FormItem>
            </div>
            <Divider orientation='top'>{t('Runtime')}</Divider>
            <div className='bfc' style={{ width: '660px', marginBottom: '36px' }}>
                {!!builtInRuntime && (
                    <FormItemLabel label={t('Runtime Type')}>
                        <div style={{ marginTop: '8px' }} />
                        <FormSelect
                            clearable={false}
                            value={type}
                            onChange={(value: string) => {
                                setType(value)
                            }}
                            options={[
                                {
                                    label: RuntimeType.BUILTIN,
                                    id: RuntimeType.BUILTIN,
                                },
                                {
                                    label: RuntimeType.OTHER,
                                    id: RuntimeType.OTHER,
                                },
                            ]}
                        />
                    </FormItemLabel>
                )}
                {type === RuntimeType.BUILTIN && (
                    <>
                        <label
                            htmlFor='l-built-in'
                            style={{ display: 'flex', alignItems: 'center', gap: 4, padding: '10px 0px' }}
                        >
                            * {t('Runtime Version')}
                        </label>
                        <p
                            id='l-built-in'
                            style={{
                                padding: '5px 20px',
                                borderRadius: '4px',
                                border: '1px solid #E2E7F0',
                            }}
                        >
                            {builtInRuntime}
                        </p>
                    </>
                )}
                {(type === RuntimeType.OTHER || !builtInRuntime) && (
                    <FormItem label={t('Runtime Version')} name='runtimeVersionUrl' required>
                        <RuntimeTreeSelector projectId={projectId} />
                    </FormItem>
                )}
            </div>
            <FormItem>
                <div style={{ display: 'flex', gap: 20, marginTop: 60 }}>
                    <div style={{ flexGrow: 1 }} />
                    <Button
                        kind='secondary'
                        type='button'
                        onClick={() => {
                            history.goBack()
                        }}
                    >
                        {t('Cancel')}
                    </Button>
                    <Button isLoading={loading} disabled={!isModified(job, values)}>
                        {t('submit')}
                    </Button>
                </div>
            </FormItem>
        </Form>
    )
}
