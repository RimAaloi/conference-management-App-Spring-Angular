import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Review } from '../models';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class ReviewService {
    private apiUrl = `${environment.apiUrl}/api/reviews`;

    constructor(private http: HttpClient) { }

    getAllReviews(): Observable<Review[]> {
        return this.http.get<Review[]>(this.apiUrl);
    }

    getReviewById(id: number): Observable<Review> {
        return this.http.get<Review>(`${this.apiUrl}/${id}`);
    }

    updateReview(id: number, review: Review): Observable<Review> {
        return this.http.put<Review>(`${this.apiUrl}/${id}`, review);
    }

    deleteReview(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
