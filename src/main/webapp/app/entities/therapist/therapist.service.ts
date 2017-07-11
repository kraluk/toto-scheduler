import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Therapist } from './therapist.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TherapistService {

    private resourceUrl = 'api/therapists';

    constructor(private http: Http) { }

    create(therapist: Therapist): Observable<Therapist> {
        const copy = this.convert(therapist);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(therapist: Therapist): Observable<Therapist> {
        const copy = this.convert(therapist);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Therapist> {
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

    private convert(therapist: Therapist): Therapist {
        const copy: Therapist = Object.assign({}, therapist);
        return copy;
    }
}
