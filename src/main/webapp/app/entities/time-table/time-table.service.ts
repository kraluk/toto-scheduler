import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { TimeTable } from './time-table.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TimeTableService {

    private resourceUrl = SERVER_API_URL + 'api/time-tables';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/time-tables';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(timeTable: TimeTable): Observable<TimeTable> {
        const copy = this.convert(timeTable);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(timeTable: TimeTable): Observable<TimeTable> {
        const copy = this.convert(timeTable);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<TimeTable> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to TimeTable.
     */
    private convertItemFromServer(json: any): TimeTable {
        const entity: TimeTable = Object.assign(new TimeTable(), json);
        entity.startDate = this.dateUtils
            .convertLocalDateFromServer(json.startDate);
        entity.endDate = this.dateUtils
            .convertLocalDateFromServer(json.endDate);
        return entity;
    }

    /**
     * Convert a TimeTable to a JSON which can be sent to the server.
     */
    private convert(timeTable: TimeTable): TimeTable {
        const copy: TimeTable = Object.assign({}, timeTable);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(timeTable.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(timeTable.endDate);
        return copy;
    }
}
