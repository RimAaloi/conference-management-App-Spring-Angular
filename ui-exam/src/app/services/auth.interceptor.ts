import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable, from, switchMap } from 'rxjs';
import { KeycloakService } from './keycloak.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    constructor(private keycloakService: KeycloakService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // Only add token for API calls
        if (request.url.includes('/api/')) {
            return from(this.keycloakService.refreshToken()).pipe(
                switchMap(() => {
                    const token = this.keycloakService.getToken();
                    if (token) {
                        request = request.clone({
                            setHeaders: {
                                Authorization: `Bearer ${token}`
                            }
                        });
                    }
                    return next.handle(request);
                })
            );
        }
        return next.handle(request);
    }
}
