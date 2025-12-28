import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReviewService, ConferenceService } from '../../services';
import { Review, Conference } from '../../models';

@Component({
    selector: 'app-reviews',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './reviews.component.html',
    styleUrl: './reviews.component.css'
})
export class ReviewsComponent implements OnInit {
    reviews = signal<Review[]>([]);
    conferences = signal<Conference[]>([]);
    loading = signal(true);
    showEditModal = signal(false);

    currentReview: Review = { texte: '', note: 5 };

    successMessage = signal('');
    errorMessage = signal('');

    constructor(
        private reviewService: ReviewService,
        private conferenceService: ConferenceService
    ) { }

    ngOnInit() {
        this.loadData();
    }

    loadData() {
        this.loading.set(true);

        this.conferenceService.getAllConferences().subscribe({
            next: (data) => this.conferences.set(data),
            error: (err) => console.error('Error loading conferences:', err)
        });

        this.reviewService.getAllReviews().subscribe({
            next: (data) => {
                this.reviews.set(data);
                this.loading.set(false);
            },
            error: (err) => {
                console.error('Error loading reviews:', err);
                this.errorMessage.set('Erreur lors du chargement des reviews');
                this.loading.set(false);
            }
        });
    }

    openEditModal(review: Review) {
        this.currentReview = { ...review };
        this.showEditModal.set(true);
    }

    closeModal() {
        this.showEditModal.set(false);
        this.currentReview = { texte: '', note: 5 };
    }

    updateReview() {
        if (this.currentReview.id) {
            this.reviewService.updateReview(this.currentReview.id, this.currentReview).subscribe({
                next: () => {
                    this.successMessage.set('Review mise à jour avec succès');
                    this.loadData();
                    this.closeModal();
                    this.clearMessages();
                },
                error: (err) => {
                    console.error('Error updating review:', err);
                    this.errorMessage.set('Erreur lors de la mise à jour');
                }
            });
        }
    }

    deleteReview(review: Review) {
        if (confirm('Êtes-vous sûr de vouloir supprimer cette review ?')) {
            this.reviewService.deleteReview(review.id!).subscribe({
                next: () => {
                    this.successMessage.set('Review supprimée avec succès');
                    this.loadData();
                    this.clearMessages();
                },
                error: (err) => {
                    console.error('Error deleting review:', err);
                    this.errorMessage.set('Erreur lors de la suppression');
                }
            });
        }
    }

    setRating(rating: number) {
        this.currentReview.note = rating;
    }

    clearMessages() {
        setTimeout(() => {
            this.successMessage.set('');
            this.errorMessage.set('');
        }, 3000);
    }

    getConferenceTitle(conferenceId: number | undefined): string {
        if (!conferenceId) return 'N/A';
        const conference = this.conferences().find(c => c.id === conferenceId);
        return conference?.titre || 'N/A';
    }

    getStarArray(note: number): boolean[] {
        return Array(5).fill(false).map((_, i) => i < note);
    }
}
