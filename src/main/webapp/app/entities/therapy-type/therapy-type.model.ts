import { BaseEntity } from './../../shared';

export class TherapyType implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public comment?: string,
    ) {
    }
}
