import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export type Plan = 'LIGHT' | 'PRO' | 'POWER';

export interface CheckoutResponse {
  initPoint: string;
  purchaseId: number;
}

@Injectable({ providedIn: 'root' })
export class CheckoutService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/checkout`;

  createCheckout(email: string, plan: Plan): Observable<CheckoutResponse> {
    return this.http.post<CheckoutResponse>(this.url, { email, plan });
  }
}
