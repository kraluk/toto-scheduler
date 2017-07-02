import { BaseEntity } from './../../shared';

export class Period implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public comment?: string,
        public startDate?: any,
        public endDate?: any,
    ) {
    }
}
