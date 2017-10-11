import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Therapy } from './therapy.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TherapyService {

    private resourceUrl = SERVER_API_URL + 'api/therapies';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/therapies';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(therapy: Therapy): Observable<Therapy> {
        const copy = this.convert(therapy);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(therapy: Therapy): Observable<Therapy> {
        const copy = this.convert(therapy);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Therapy> {
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
     * Convert a returned JSON object to Therapy.
     */
    private convertItemFromServer(json: any): Therapy {
        const entity: Therapy = Object.assign(new Therapy(), json);
        entity.date = this.dateUtils
            .convertDateTimeFromServer(json.date);
        return entity;
    }

    /**
     * Convert a Therapy to a JSON which can be sent to the server.
     */
    private convert(therapy: Therapy): Therapy {
        const copy: Therapy = Object.assign({}, therapy);

        copy.date = this.dateUtils.toDate(therapy.date);
        return copy;
    }
}
