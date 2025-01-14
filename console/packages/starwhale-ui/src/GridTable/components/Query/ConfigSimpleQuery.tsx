import React, { useEffect } from 'react'
import { ColumnT, QueryT } from '@starwhale/ui/base/data-table/types'
import _ from 'lodash'
import { createForm } from '@starwhale/ui/Form/forms'

const { Form, FormItem, useForm } = createForm<Record<string, any>>()

type PropsT = {
    columns?: ColumnT[]
    value: QueryT[]
    onChange: (args: QueryT[]) => void
}

function ConfigSimpleQuery({ columns, onChange, value }: PropsT) {
    const [values, setValues] = React.useState<Record<string, any> | undefined>(undefined)

    const [form] = useForm()

    useEffect(() => {
        const tmp = _.fromPairs(
            value.map((query) => {
                return [query.property, query.value]
            })
        )
        setValues(tmp)
        form.setFieldsValue(tmp)
    }, [value, form])

    const handleValuesChange = React.useCallback(
        (_changes, values_: Record<string, any>) => {
            onChange?.(
                Object.entries(values_)
                    .filter((v) => !!v)
                    .map(([key, v]) => ({
                        value: v,
                        op: 'EQUAL',
                        property: key,
                    }))
            )
        },
        [onChange]
    )

    const columnsWithFilter = React.useMemo(() => {
        return columns?.filter((column) => column.filterable)
    }, [columns])

    const Filters = React.useMemo(() => {
        return columnsWithFilter?.map((column) => {
            return (
                <FormItem
                    name={column.key}
                    key={column.key}
                    noStyle
                    style={{
                        marginBottom: 0,
                        minWidth: '170px',
                    }}
                >
                    {/* @ts-ignore */}
                    {column?.renderFilter()}
                </FormItem>
            )
        })
    }, [columnsWithFilter])

    return (
        <Form form={form} initialValues={values} onValuesChange={handleValuesChange}>
            <div
                data-type='config-simple-query'
                style={{
                    display: 'flex',
                    gap: 20,
                    flexWrap: 'nowrap',
                }}
            >
                {Filters}
            </div>
        </Form>
    )
}

export default ConfigSimpleQuery
