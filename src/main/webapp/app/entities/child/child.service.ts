import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Child } from './child.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ChildService {

    private resourceUrl = SERVER_API_URL + 'api/children';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/children';

    constructor(private http: Http) { }

    create(child: Child): Observable<Child> {
        const copy = this.convert(child);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(child: Child): Observable<Child> {
        const copy = this.convert(child);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Child> {
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
     * Convert a returned JSON object to Child.
     */
    private convertItemFromServer(json: any): Child {
        const entity: Child = Object.assign(new Child(), json);
        return entity;
    }

    /**
     * Convert a Child to a JSON which can be sent to the server.
     */
    private convert(child: Child): Child {
        const copy: Child = Object.assign({}, child);
        return copy;
    }
}
