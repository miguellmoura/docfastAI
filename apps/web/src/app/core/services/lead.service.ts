import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class LeadService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/leads`;

  capture(email: string, source = 'smart-gate'): Observable<{ ok: boolean }> {
    return this.http.post<{ ok: boolean }>(this.url, { email, source });
  }
}
