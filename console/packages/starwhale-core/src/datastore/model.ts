/* eslint-disable */
interface ISwType {
    name: string
    decode(value: any): any
    encode(value: any): any
    serialize(value: any): any
    // merge(type: SwType): SwType
    toString(): string
}

export class SwType implements ISwType {
    name: string
    constructor(name: string) {
        this.name = name
    }
    decode(value: any) {
        throw new Error('Method not implemented.')
    }
    encode(value: any) {
        throw new Error('Method not implemented.')
    }
    serialize(value: any) {
        return value
    }
    static decode_schema(schema: any, onlyValue = true): any {
        if (schema.type === 'MAP') {
            const t = new SwMapType(schema.value)
            return {
                ...schema,
                value: onlyValue ? t.value : t,
            }
        }
        if (schema.type === 'LIST') {
            const t = new SwListType(schema.value)
            return {
                ...schema,
                value: onlyValue ? t.value : t,
            }
        }
        if (schema.type === 'BYTES') {
            return {
                ...schema,
                value: 'BYTES',
            }
        }
        if (schema.type === 'TUPLE') {
            return {
                ...schema,
                value: 'TUPLE',
            }
        }
        if (schema.type === 'OBJECT') {
            const t = new SwObjectType(Object, schema.value)
            return {
                ...schema,
                value: onlyValue ? t.value : t,
            }
        }
        return schema
    }

    toString(): string {
        return this.name
    }
}

export class SwCompositeType extends SwType {
    constructor(name: string) {
        super(name)
    }
    serialize(value: any) {
        return JSON.stringify(value)
    }
}

export class SwListType extends SwCompositeType {
    constructor(public value_schema: any) {
        super('list')
        this.value_schema = value_schema
    }

    decode(value: any): any {
        if (!Array.isArray(value)) {
            throw new Error(`Value ${value} is not an array`)
        }
        return value.map((v) => SwType.decode_schema(v).value)
    }

    get value(): any {
        return this.decode(this.value_schema)
    }

    toString(): string {
        return JSON.stringify(this.decode(this.value_schema))
    }
}

/**
     *
     *     "sys/dataset_id_version_map": {
            "value": [{
                "key": {
                    "type": "INT64",
                    "value": "1"
                },
                "value": {
                    "type": "STRING",
                    "value": "bicm3c23utaujr5nvbgzwp3vmwlnhfgm3yzc2c3r"
                }
            }],
            "type": "MAP"
        },
 */
interface SwMapItem {
    key: any
    value: any
}

export class SwMapType extends SwCompositeType {
    private value_schema: any

    constructor(value_schema: any) {
        super('map')
        this.value_schema = value_schema
    }

    decode(value: any): any {
        if (!value || typeof value !== 'object') {
            throw new Error(`Value ${value} is not an object`)
        }
        const res = new Map()
        for (const [_, v] of Object.entries(value)) {
            const item = v as SwMapItem
            res.set(SwType.decode_schema(item['key']).value, SwType.decode_schema(item['value']).value)
        }
        return res
    }

    get value(): any {
        return Object.fromEntries(this.decode(this.value_schema))
    }

    toString(): string {
        return JSON.stringify(Object.fromEntries(this.decode(this.value_schema)))
    }
}

export class SwObjectType extends SwCompositeType {
    private raw_type: any
    private attrs: { [key: string]: SwType }

    constructor(raw_type: any, attrs: { [key: string]: SwType }) {
        super('object')
        this.raw_type = raw_type
        this.attrs = attrs
    }

    decode(value: any): any {
        if (value === null || typeof value !== 'object') {
            return null
        }

        if (typeof value === 'object' && !Array.isArray(value)) {
            const ret = new this.raw_type()
            for (const k in value) {
                const v = value[k]
                const type = this.attrs[k]
                if (type === undefined) {
                    throw new Error(`invalid attribute ${k}`)
                }
                ret[k] = SwType.decode_schema(v).value
            }
            return ret
        }
        throw new Error(`value should be a object: ${value}`)
    }

    get value(): any {
        return this.decode(this.attrs)
    }

    toString(): string {
        return JSON.stringify(this.decode(this.attrs))
    }
}
// interface SwTupleElementType {
//     name: string
//     type: SwType
// }

// export class SwTupleType extends SwCompositeType {
//     constructor(public element_type: SwTupleElementType[]) {}

//     get name(): string {
//         return `tuple<${this.element_type.map((et) => `${et.name}:${et.type.toString()}`).join(', ')}>`
//     }

//     merge(type: SwType): SwType {
//         if (type instanceof SwTupleType) {
//             const et: SwTupleElementType[] = []
//             for (let i = 0; i < this.element_type.length; i++) {
//                 et.push({
//                     name: this.element_type[i].name,
//                     type: this.element_type[i].type.merge(type.element_type[i].type),
//                 })
//             }
//             return new SwTupleType(et)
//         }
//         throw new Error(`Can not merge tuple with ${type.name}`)
//     }

//     encode(value: any): any {
//         if (!Array.isArray(value)) {
//             throw new Error(`Value ${value} is not an array`)
//         }
//         if (value.length !== this.element_type.length) {
//             throw new Error(`Value ${value} has length ${value.length}, expected length ${this.element_type.length}`)
//         }
//         const res: Record<string, any> = {}
//         for (let i = 0; i < value.length; i++) {
//             const name = this.element_type[i].name
//             const encoded = this.element_type[i].type.encode(value[i])
//             if (encoded) {
//                 res[name] = encoded
//             } else {
//                 res[name] = null
//             }
//         }
//         return res
//     }

//     decode(value: any): any {
//         if (!value || typeof value !== 'object') {
//             throw new Error(`Value ${value} is not an object`)
//         }
//         const res = []
//         for (const et of this.element_type) {
//             res.push(et.type.decode(value[et.name]))
//         }
//         return res
//     }

//     toString(): string {
//         return `tuple<${this.element_type.map((et) => `${et.name}:${et.type.toString()}`).join(', ')}>`
//     }
// }
