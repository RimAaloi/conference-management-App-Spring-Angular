import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { KeynoteService, ConferenceService } from '../../services';
import { Keynote, Conference } from '../../models';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './dashboard.component.html',
    styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
    keynotes = signal<Keynote[]>([]);
    conferences = signal<Conference[]>([]);
    loading = signal(true);

    stats = signal({
        totalKeynotes: 0,
        totalConferences: 0,
        totalReviews: 0,
        avgScore: 0
    });

    constructor(
        private keynoteService: KeynoteService,
        private conferenceService: ConferenceService
    ) { }

    ngOnInit() {
        this.loadData();
    }

    loadData() {
        this.loading.set(true);

        this.keynoteService.getAllKeynotes().subscribe({
            next: (data) => {
                this.keynotes.set(data);
                this.updateStats();
            },
            error: (err) => console.error('Error loading keynotes:', err)
        });

        this.conferenceService.getAllConferences().subscribe({
            next: (data) => {
                this.conferences.set(data);
                this.updateStats();
                this.loading.set(false);
            },
            error: (err) => {
                console.error('Error loading conferences:', err);
                this.loading.set(false);
            }
        });
    }

    updateStats() {
        const conferences = this.conferences();
        const totalReviews = conferences.reduce((sum, c) => sum + (c.reviews?.length || 0), 0);
        const scores = conferences.filter(c => c.score).map(c => c.score!);
        const avgScore = scores.length > 0 ? scores.reduce((a, b) => a + b, 0) / scores.length : 0;

        this.stats.set({
            totalKeynotes: this.keynotes().length,
            totalConferences: conferences.length,
            totalReviews,
            avgScore: Math.round(avgScore * 10) / 10
        });
    }

    getRecentConferences(): Conference[] {
        return this.conferences()
            .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
            .slice(0, 5);
    }

    getStarArray(score: number | undefined): boolean[] {
        const rating = Math.round(score || 0);
        return Array(5).fill(false).map((_, i) => i < rating);
    }
}
