import { WidgetBaseConfig, WidgetConfig, WidgetGroupType, WidgetMeta } from '../types/index'
import { WidgetType } from '../widget/WidgetFactory'
import WidgetFactory from '@starwhale/core/widget/WidgetFactory'
import { generateId } from '../utils/generators'
import { Matcher, PANEL_DYNAMIC_MATCHES, replacer, Replacer } from '../utils/replacer'
import { klona } from 'klona/full'
import _ from 'lodash'
import { StoreType } from '../context'
import { WidgetStoreState } from '../store/store'

const PersistProperty = {
    type: true,
    name: true,
    group: true,
    description: true,
}

class WidgetModel implements WidgetConfig {
    id!: string
    type!: WidgetType
    name!: string
    group?: WidgetGroupType
    description?: string
    meta?: WidgetMeta

    defaultConfig!: WidgetConfig
    restoreConfig!: WidgetConfig
    overrideConfig!: WidgetConfig

    replacer!: Replacer
    dynamicVars!: Record<string, any>

    constructor(config: any) {
        this.restore(klona(config))
        this.replacer = replacer(PANEL_DYNAMIC_MATCHES)
    }

    restore(config: any) {
        const { type, id } = config
        const properties = Object.assign({}, config)
        this.restoreConfig = properties

        if (type && !this.defaultConfig && WidgetFactory.getWidget(type)) {
            this.defaultConfig = (WidgetFactory.getWidget(type) as any)?.defaults
        }
        if (!id) {
            this.generateId()
        }
        // @FIXME check properties
        for (const key in properties) {
            ;(this as any)[key] = properties[key]
        }
    }

    setDynamicVars(globalDynamicVars: any) {
        this.dynamicVars = globalDynamicVars
    }

    setOverrides(config: any) {
        this.overrideConfig = this.replacer.toOrigin(config, {
            ...this.dynamicVars,
            ...(config?.dynamicConfig?.data ?? {}),
        })
        // @TODO set dynamicConfig data
        // console.log('------', this.replacer.toTemplate(this.overrideConfig))
    }

    updateFieldConfig(fieldConfig: any) {
        this.setOverrides(_.merge({}, this.overrideConfig, { fieldConfig }))
    }

    updateOptionConfig(optionConfig: any) {
        this.setOverrides(_.merge({}, this.overrideConfig, { optionConfig }))
    }

    toSave() {
        return {
            overrides: {
                [this.id]: this.replacer.toTemplate(this.overrideConfig),
            },
        }
    }

    generateId() {
        this.id = generateId(this.defaultConfig?.group ?? '')
    }

    get config() {
        return _.merge({}, this.defaultConfig, this.overrideConfig)
    }

    get defaults() {
        return this.defaultConfig
    }

    get overrides() {
        return {
            id: this.id,
        }
    }

    getPersistProperty(): WidgetConfig {
        const properties = {} as WidgetConfig
        for (const key in PersistProperty) {
            ;(properties as any)[key] = (this as any)[key]
        }
        return properties
    }

    saveToStore(api: WidgetStoreState) {
        api.onConfigChange(['widgets', this.id, 'optionConfig'], this.overrideConfig)
    }
}

export default WidgetModel