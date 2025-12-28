import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ConferenceService, KeynoteService } from '../../services';
import { Conference, ConferenceType, Keynote, Review } from '../../models';

@Component({
    selector: 'app-conferences',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './conferences.component.html',
    styleUrl: './conferences.component.css'
})
export class ConferencesComponent implements OnInit {
    conferences = signal<Conference[]>([]);
    keynotes = signal<Keynote[]>([]);
    loading = signal(true);
    showModal = signal(false);
    showReviewModal = signal(false);
    showDetailModal = signal(false);
    editMode = signal(false);

    currentConference: Conference = this.getEmptyConference();
    selectedConference = signal<Conference | null>(null);
    newReview: Review = { texte: '', note: 5 };

    searchTerm = '';
    filterType: ConferenceType | '' = '';

    successMessage = signal('');
    errorMessage = signal('');

    conferenceTypes: ConferenceType[] = ['ACADEMIC', 'COMMERCIAL'];

    constructor(
        private conferenceService: ConferenceService,
        private keynoteService: KeynoteService
    ) { }

    ngOnInit() {
        this.loadData();
    }

    getEmptyConference(): Conference {
        return {
            titre: '',
            type: 'ACADEMIC',
            date: new Date().toISOString().split('T')[0],
            duree: 60,
            nombreInscrits: 0,
            keynoteId: undefined
        };
    }

    loadData() {
        this.loading.set(true);

        this.keynoteService.getAllKeynotes().subscribe({
            next: (data) => this.keynotes.set(data),
            error: (err) => console.error('Error loading keynotes:', err)
        });

        this.conferenceService.getAllConferences().subscribe({
            next: (data) => {
                this.conferences.set(data);
                this.loading.set(false);
            },
            error: (err) => {
                console.error('Error loading conferences:', err);
                this.errorMessage.set('Erreur lors du chargement des conférences');
                this.loading.set(false);
            }
        });
    }

    openCreateModal() {
        this.editMode.set(false);
        this.currentConference = this.getEmptyConference();
        this.showModal.set(true);
    }

    openEditModal(conference: Conference) {
        this.editMode.set(true);
        this.currentConference = { ...conference };
        this.showModal.set(true);
    }

    openDetailModal(conference: Conference) {
        this.selectedConference.set(conference);
        this.showDetailModal.set(true);
    }

    openReviewModal(conference: Conference) {
        this.selectedConference.set(conference);
        this.newReview = { texte: '', note: 5 };
        this.showReviewModal.set(true);
    }

    closeModal() {
        this.showModal.set(false);
        this.showReviewModal.set(false);
        this.showDetailModal.set(false);
        this.currentConference = this.getEmptyConference();
    }

    saveConference() {
        if (this.editMode() && this.currentConference.id) {
            this.conferenceService.updateConference(this.currentConference.id, this.currentConference).subscribe({
                next: () => {
                    this.successMessage.set('Conférence mise à jour avec succès');
                    this.loadData();
                    this.closeModal();
                    this.clearMessages();
                },
                error: (err) => {
                    console.error('Error updating conference:', err);
                    this.errorMessage.set('Erreur lors de la mise à jour');
                }
            });
        } else {
            this.conferenceService.createConference(this.currentConference).subscribe({
                next: () => {
                    this.successMessage.set('Conférence créée avec succès');
                    this.loadData();
                    this.closeModal();
                    this.clearMessages();
                },
                error: (err) => {
                    console.error('Error creating conference:', err);
                    this.errorMessage.set('Erreur lors de la création');
                }
            });
        }
    }

    deleteConference(conference: Conference) {
        if (confirm(`Êtes-vous sûr de vouloir supprimer "${conference.titre}" ?`)) {
            this.conferenceService.deleteConference(conference.id!).subscribe({
                next: () => {
                    this.successMessage.set('Conférence supprimée avec succès');
                    this.loadData();
                    this.clearMessages();
                },
                error: (err) => {
                    console.error('Error deleting conference:', err);
                    this.errorMessage.set('Erreur lors de la suppression');
                }
            });
        }
    }

    addReview() {
        const conference = this.selectedConference();
        if (conference && conference.id) {
            this.conferenceService.addReview(conference.id, this.newReview).subscribe({
                next: () => {
                    this.successMessage.set('Review ajoutée avec succès');
                    this.loadData();
                    this.closeModal();
                    this.clearMessages();
                },
                error: (err) => {
                    console.error('Error adding review:', err);
                    this.errorMessage.set('Erreur lors de l\'ajout de la review');
                }
            });
        }
    }

    setRating(rating: number) {
        this.newReview.note = rating;
    }

    clearMessages() {
        setTimeout(() => {
            this.successMessage.set('');
            this.errorMessage.set('');
        }, 3000);
    }

    getFilteredConferences(): Conference[] {
        let result = this.conferences();

        if (this.searchTerm.trim()) {
            const term = this.searchTerm.toLowerCase();
            result = result.filter(c => c.titre.toLowerCase().includes(term));
        }

        if (this.filterType) {
            result = result.filter(c => c.type === this.filterType);
        }

        return result;
    }

    getStarArray(score: number | undefined): boolean[] {
        const rating = Math.round(score || 0);
        return Array(5).fill(false).map((_, i) => i < rating);
    }

    getKeynoteById(id: number | undefined): Keynote | undefined {
        if (!id) return undefined;
        return this.keynotes().find(k => k.id === id);
    }
}
