import { BaseEntity } from './../../shared';

export class Therapist implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public comment?: string,
        public userId?: number,
        public therapies?: BaseEntity[],
        public roles?: BaseEntity[],
    ) {
    }
}
