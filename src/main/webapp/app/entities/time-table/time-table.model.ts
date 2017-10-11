import { BaseEntity } from './../../shared';

export class TimeTable implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public startDate?: any,
        public endDate?: any,
        public comment?: string,
        public therapies?: BaseEntity[],
        public childId?: number,
    ) {
    }
}
