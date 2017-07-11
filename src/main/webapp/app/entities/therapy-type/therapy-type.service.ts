import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { TherapyType } from './therapy-type.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TherapyTypeService {

    private resourceUrl = 'api/therapy-types';

    constructor(private http: Http) { }

    create(therapyType: TherapyType): Observable<TherapyType> {
        const copy = this.convert(therapyType);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(therapyType: TherapyType): Observable<TherapyType> {
        const copy = this.convert(therapyType);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<TherapyType> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(therapyType: TherapyType): TherapyType {
        const copy: TherapyType = Object.assign({}, therapyType);
        return copy;
    }
}
