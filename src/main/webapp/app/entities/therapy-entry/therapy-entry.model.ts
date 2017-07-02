import { BaseEntity } from './../../shared';

export class TherapyEntry implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
    ) {
    }
}
