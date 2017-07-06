import { BaseEntity } from './../../shared';

export class Child implements BaseEntity {
    constructor(
        public id?: number,
        public registerNumber?: string,
        public name?: string,
        public comment?: string,
        public therapies?: BaseEntity[],
    ) {
    }
}
