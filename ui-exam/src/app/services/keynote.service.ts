import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Keynote } from '../models';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class KeynoteService {
    private apiUrl = `${environment.apiUrl}/api/keynotes`;

    constructor(private http: HttpClient) { }

    getAllKeynotes(): Observable<Keynote[]> {
        return this.http.get<Keynote[]>(this.apiUrl);
    }

    getKeynoteById(id: number): Observable<Keynote> {
        return this.http.get<Keynote>(`${this.apiUrl}/${id}`);
    }

    createKeynote(keynote: Keynote): Observable<Keynote> {
        return this.http.post<Keynote>(this.apiUrl, keynote);
    }

    updateKeynote(id: number, keynote: Keynote): Observable<Keynote> {
        return this.http.put<Keynote>(`${this.apiUrl}/${id}`, keynote);
    }

    deleteKeynote(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    searchByNom(nom: string): Observable<Keynote[]> {
        return this.http.get<Keynote[]>(`${this.apiUrl}/search/nom?nom=${nom}`);
    }

    searchByFonction(fonction: string): Observable<Keynote[]> {
        return this.http.get<Keynote[]>(`${this.apiUrl}/search/fonction?fonction=${fonction}`);
    }
}
