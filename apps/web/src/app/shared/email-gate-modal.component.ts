import { Component, EventEmitter, Output, signal, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GateService } from '../core/services/gate.service';
import { LeadService } from '../core/services/lead.service';

@Component({
  selector: 'app-email-gate-modal',
  standalone: true,
  imports: [FormsModule],
  template: `
    <div
      class="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/50 px-4 py-6"
      (click)="onBackdrop($event)"
    >
      <div class="card w-full max-w-md p-6 sm:p-8 relative" (click)="$event.stopPropagation()">
        <button
          (click)="closed.emit()"
          class="absolute top-3 right-3 p-1.5 text-ink-500 hover:text-ink-900 rounded-md hover:bg-slate-100"
          aria-label="Fechar"
        >
          <svg viewBox="0 0 24 24" class="h-5 w-5" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round">
            <path d="M18 6L6 18M6 6l12 12" />
          </svg>
        </button>

        <div class="h-12 w-12 rounded-xl bg-brand-50 flex items-center justify-center mb-4">
          <svg viewBox="0 0 24 24" class="h-6 w-6 text-brand-600" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z" />
            <polyline points="22,6 12,13 2,6" />
          </svg>
        </div>

        <h3 class="text-xl font-bold leading-tight">
          Você já viu como funciona.
        </h3>
        <p class="text-ink-700 mt-2 text-sm">
          Para mais análises grátis, deixe seu melhor e-mail.<br>
          Sem spam. Sem senha.
        </p>

        @if (errorMsg()) {
          <p class="mt-3 text-sm text-red-700 bg-red-50 border border-red-200 rounded-lg px-3 py-2">
            {{ errorMsg() }}
          </p>
        }

        <form (ngSubmit)="submit()" class="mt-5 space-y-3">
          <input
            type="email"
            name="email"
            [(ngModel)]="email"
            required
            placeholder="seu@email.com"
            autocomplete="email"
            inputmode="email"
            class="w-full rounded-xl border border-slate-200 px-4 py-3 text-base focus:border-brand-500 focus:ring-2 focus:ring-brand-200 outline-none"
            [disabled]="isSubmitting()"
          />
          <button
            type="submit"
            class="btn-primary w-full"
            [disabled]="isSubmitting() || !isValid()"
          >
            @if (isSubmitting()) {
              <svg class="animate-spin h-4 w-4" viewBox="0 0 24 24" fill="none">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
              </svg>
              Enviando...
            } @else {
              Continuar análise →
            }
          </button>
        </form>

        <p class="text-xs text-ink-500 mt-4 text-center">
          🔒 Usamos seu e-mail só para enviar o link de acesso e seus resultados.
        </p>
      </div>
    </div>
  `,
})
export class EmailGateModalComponent {
  private readonly gate = inject(GateService);
  private readonly leadService = inject(LeadService);

  @Output() closed = new EventEmitter<void>();
  @Output() confirmed = new EventEmitter<void>();

  email = '';
  readonly isSubmitting = signal(false);
  readonly errorMsg = signal<string | null>(null);

  isValid(): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email.trim());
  }

  submit(): void {
    if (!this.isValid() || this.isSubmitting()) return;
    this.isSubmitting.set(true);
    this.errorMsg.set(null);

    this.leadService.capture(this.email, 'smart-gate-2nd').subscribe({
      next: () => {
        this.gate.saveEmail(this.email);
        this.isSubmitting.set(false);
        this.confirmed.emit();
      },
      error: () => {
        // Falha de rede não pode bloquear conversão. Salva local e segue.
        this.gate.saveEmail(this.email);
        this.isSubmitting.set(false);
        this.confirmed.emit();
      },
    });
  }

  onBackdrop(event: MouseEvent): void {
    if (event.target === event.currentTarget) this.closed.emit();
  }
}
