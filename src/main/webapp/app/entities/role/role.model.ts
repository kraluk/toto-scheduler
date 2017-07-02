import { BaseEntity } from './../../shared';

export class Role implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public userId?: number,
    ) {
    }
}
