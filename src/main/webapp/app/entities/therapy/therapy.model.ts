import { BaseEntity } from './../../shared';

export class Therapy implements BaseEntity {
    constructor(
        public id?: number,
        public comment?: string,
        public date?: any,
        public therapyEntryId?: number,
        public userId?: number,
        public periodId?: number,
        public childId?: number,
    ) {
    }
}
