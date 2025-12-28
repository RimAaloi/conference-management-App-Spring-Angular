import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { KeynotesComponent } from './components/keynotes/keynotes.component';
import { ConferencesComponent } from './components/conferences/conferences.component';
import { ReviewsComponent } from './components/reviews/reviews.component';

export const routes: Routes = [
    { path: '', component: DashboardComponent },
    { path: 'keynotes', component: KeynotesComponent },
    { path: 'conferences', component: ConferencesComponent },
    { path: 'reviews', component: ReviewsComponent },
    { path: '**', redirectTo: '' }
];
