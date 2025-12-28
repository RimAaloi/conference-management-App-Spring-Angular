import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Conference, ConferenceType, Review } from '../models';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class ConferenceService {
    private apiUrl = `${environment.apiUrl}/api/conferences`;

    constructor(private http: HttpClient) { }

    getAllConferences(withKeynotes: boolean = true): Observable<Conference[]> {
        return this.http.get<Conference[]>(`${this.apiUrl}?withKeynotes=${withKeynotes}`);
    }

    getConferenceById(id: number, withKeynote: boolean = true): Observable<Conference> {
        return this.http.get<Conference>(`${this.apiUrl}/${id}?withKeynote=${withKeynote}`);
    }

    createConference(conference: Conference): Observable<Conference> {
        return this.http.post<Conference>(this.apiUrl, conference);
    }

    updateConference(id: number, conference: Conference): Observable<Conference> {
        return this.http.put<Conference>(`${this.apiUrl}/${id}`, conference);
    }

    deleteConference(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    getConferencesByType(type: ConferenceType): Observable<Conference[]> {
        return this.http.get<Conference[]>(`${this.apiUrl}/type/${type}`);
    }

    searchByTitre(titre: string): Observable<Conference[]> {
        return this.http.get<Conference[]>(`${this.apiUrl}/search?titre=${titre}`);
    }

    getConferencesByKeynoteId(keynoteId: number): Observable<Conference[]> {
        return this.http.get<Conference[]>(`${this.apiUrl}/keynote/${keynoteId}`);
    }

    // Review methods
    getReviewsByConferenceId(conferenceId: number): Observable<Review[]> {
        return this.http.get<Review[]>(`${this.apiUrl}/${conferenceId}/reviews`);
    }

    addReview(conferenceId: number, review: Review): Observable<Review> {
        return this.http.post<Review>(`${this.apiUrl}/${conferenceId}/reviews`, review);
    }
}
