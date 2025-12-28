import { Keynote } from './keynote.model';
import { Review } from './review.model';

export type ConferenceType = 'ACADEMIC' | 'COMMERCIAL';

export interface Conference {
    id?: number;
    titre: string;
    type: ConferenceType;
    date: string;
    duree: number;
    nombreInscrits: number;
    score?: number;
    keynoteId?: number;
    keynote?: Keynote;
    reviews?: Review[];
}
