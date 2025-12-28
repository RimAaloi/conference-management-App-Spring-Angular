import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({
    providedIn: 'root'
})
export class KeycloakService {
    private keycloak: Keycloak | undefined;
    private authenticated = false;

    async init(): Promise<boolean> {
        this.keycloak = new Keycloak({
            url: 'http://localhost:8180',
            realm: 'conference-realm',
            clientId: 'angular-client'
        });

        try {
            this.authenticated = await this.keycloak.init({
                onLoad: 'login-required',
                checkLoginIframe: false
            });
            console.log('Keycloak authentication:', this.authenticated ? 'SUCCESS' : 'FAILED');
            return this.authenticated;
        } catch (error) {
            console.error('Keycloak init error:', error);
            return false;
        }
    }

    isAuthenticated(): boolean {
        return this.authenticated;
    }

    getToken(): string | undefined {
        return this.keycloak?.token;
    }

    getUsername(): string | undefined {
        return this.keycloak?.tokenParsed?.['preferred_username'];
    }

    getRoles(): string[] {
        return this.keycloak?.tokenParsed?.['realm_access']?.['roles'] || [];
    }

    async logout(): Promise<void> {
        await this.keycloak?.logout({ redirectUri: window.location.origin });
    }

    async refreshToken(): Promise<boolean> {
        try {
            const refreshed = await this.keycloak?.updateToken(30);
            return refreshed || false;
        } catch (error) {
            console.error('Token refresh failed:', error);
            return false;
        }
    }
}
