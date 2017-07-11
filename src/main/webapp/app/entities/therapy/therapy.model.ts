import { BaseEntity } from './../../shared';

export class Therapy implements BaseEntity {
    constructor(
        public id?: number,
        public date?: any,
        public comment?: string,
        public therapyTypeId?: number,
        public therapistId?: number,
        public timeTableId?: number,
    ) {
    }
}
