import { Injectable, signal, computed } from '@angular/core';

const STORAGE_COUNT = 'docfast_analysis_count';
const STORAGE_EMAIL = 'docfast_user_email';

export type GateAction = 'PROCEED' | 'SHOW_EMAIL_GATE' | 'SHOW_PAYWALL';

@Injectable({ providedIn: 'root' })
export class GateService {
  private readonly _count = signal<number>(this.readCount());
  private readonly _email = signal<string | null>(this.readEmail());

  readonly count = this._count.asReadonly();
  readonly email = this._email.asReadonly();
  readonly hasEmail = computed(() => this._email() !== null);

  /**
   * Decide o que fazer ANTES de iniciar uma análise.
   * - 1ª análise (count=0): livre, anônima
   * - 2ª análise (count=1): exigir e-mail (se ainda não tem)
   * - 3ª+ (count>=2): paywall
   */
  decideNextAction(): GateAction {
    const c = this._count();
    if (c === 0) return 'PROCEED';
    if (c === 1) return this._email() ? 'PROCEED' : 'SHOW_EMAIL_GATE';
    return 'SHOW_PAYWALL';
  }

  registerAnalysisCompleted(): void {
    const next = this._count() + 1;
    this._count.set(next);
    localStorage.setItem(STORAGE_COUNT, String(next));
  }

  saveEmail(email: string): void {
    const clean = email.trim().toLowerCase();
    this._email.set(clean);
    localStorage.setItem(STORAGE_EMAIL, clean);
  }

  private readCount(): number {
    const raw = localStorage.getItem(STORAGE_COUNT);
    const n = raw ? parseInt(raw, 10) : 0;
    return Number.isFinite(n) && n >= 0 ? n : 0;
  }

  private readEmail(): string | null {
    const raw = localStorage.getItem(STORAGE_EMAIL);
    return raw && raw.includes('@') ? raw : null;
  }
}
