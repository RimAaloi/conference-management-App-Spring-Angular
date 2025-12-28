import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { KeynoteService } from '../../services';
import { Keynote } from '../../models';

@Component({
    selector: 'app-keynotes',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './keynotes.component.html',
    styleUrl: './keynotes.component.css'
})
export class KeynotesComponent implements OnInit {
    keynotes = signal<Keynote[]>([]);
    loading = signal(true);
    showModal = signal(false);
    editMode = signal(false);

    currentKeynote: Keynote = this.getEmptyKeynote();
    searchTerm = '';

    successMessage = signal('');
    errorMessage = signal('');

    constructor(private keynoteService: KeynoteService) { }

    ngOnInit() {
        this.loadKeynotes();
    }

    getEmptyKeynote(): Keynote {
        return {
            nom: '',
            prenom: '',
            email: '',
            fonction: ''
        };
    }

    loadKeynotes() {
        this.loading.set(true);
        this.keynoteService.getAllKeynotes().subscribe({
            next: (data) => {
                this.keynotes.set(data);
                this.loading.set(false);
            },
            error: (err) => {
                console.error('Error loading keynotes:', err);
                this.errorMessage.set('Erreur lors du chargement des keynotes');
                this.loading.set(false);
            }
        });
    }

    openCreateModal() {
        this.editMode.set(false);
        this.currentKeynote = this.getEmptyKeynote();
        this.showModal.set(true);
    }

    openEditModal(keynote: Keynote) {
        this.editMode.set(true);
        this.currentKeynote = { ...keynote };
        this.showModal.set(true);
    }

    closeModal() {
        this.showModal.set(false);
        this.currentKeynote = this.getEmptyKeynote();
    }

    saveKeynote() {
        if (this.editMode() && this.currentKeynote.id) {
            this.keynoteService.updateKeynote(this.currentKeynote.id, this.currentKeynote).subscribe({
                next: () => {
                    this.successMessage.set('Keynote mis à jour avec succès');
                    this.loadKeynotes();
                    this.closeModal();
                    this.clearMessages();
                },
                error: (err) => {
                    console.error('Error updating keynote:', err);
                    this.errorMessage.set('Erreur lors de la mise à jour');
                }
            });
        } else {
            this.keynoteService.createKeynote(this.currentKeynote).subscribe({
                next: () => {
                    this.successMessage.set('Keynote créé avec succès');
                    this.loadKeynotes();
                    this.closeModal();
                    this.clearMessages();
                },
                error: (err) => {
                    console.error('Error creating keynote:', err);
                    this.errorMessage.set('Erreur lors de la création');
                }
            });
        }
    }

    deleteKeynote(keynote: Keynote) {
        if (confirm(`Êtes-vous sûr de vouloir supprimer ${keynote.prenom} ${keynote.nom} ?`)) {
            this.keynoteService.deleteKeynote(keynote.id!).subscribe({
                next: () => {
                    this.successMessage.set('Keynote supprimé avec succès');
                    this.loadKeynotes();
                    this.clearMessages();
                },
                error: (err) => {
                    console.error('Error deleting keynote:', err);
                    this.errorMessage.set('Erreur lors de la suppression');
                }
            });
        }
    }

    search() {
        if (this.searchTerm.trim()) {
            this.keynoteService.searchByNom(this.searchTerm).subscribe({
                next: (data) => this.keynotes.set(data),
                error: (err) => console.error('Error searching:', err)
            });
        } else {
            this.loadKeynotes();
        }
    }

    clearMessages() {
        setTimeout(() => {
            this.successMessage.set('');
            this.errorMessage.set('');
        }, 3000);
    }

    getFilteredKeynotes(): Keynote[] {
        if (!this.searchTerm.trim()) return this.keynotes();
        const term = this.searchTerm.toLowerCase();
        return this.keynotes().filter(k =>
            k.nom.toLowerCase().includes(term) ||
            k.prenom.toLowerCase().includes(term) ||
            k.fonction.toLowerCase().includes(term)
        );
    }
}
