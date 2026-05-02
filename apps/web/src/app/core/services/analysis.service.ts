import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AnalysisResponse {
  result: string;
  extractedChars: number;
  estimatedCostBrl: number;
}

export type AnalysisType = 'SUMMARIZE' | 'EXTRACT_KEY_POINTS';

@Injectable({
  providedIn: 'root'
})
export class AnalysisService {
  private readonly apiUrl = `${environment.apiUrl}/analysis`;
  
  readonly isProcessing = signal(false);
  readonly error = signal<string | null>(null);

  constructor(private http: HttpClient) {}

  analyze(file: File, type: AnalysisType = 'SUMMARIZE'): Observable<AnalysisResponse> {
    this.isProcessing.set(true);
    this.error.set(null);

    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', type);

    return this.http.post<AnalysisResponse>(this.apiUrl, formData).pipe(
      tap(() => this.isProcessing.set(false)),
      catchError(err => {
        this.isProcessing.set(false);
        const message = err.error?.detail || err.error?.message || 
                       'Erro ao processar arquivo. Tente novamente.';
        this.error.set(message);
        return throwError(() => new Error(message));
      })
    );
  }

  reset(): void {
    this.error.set(null);
  }
}
