import { BaseEntity } from './../../shared';

export class Child implements BaseEntity {
    constructor(
        public id?: number,
        public registerNumber?: string,
        public firstName?: string,
        public lastName?: string,
        public comment?: string,
        public timeTables?: BaseEntity[],
    ) {
    }
}
