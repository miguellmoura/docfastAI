import { Component, EventEmitter, Output, Input, signal, inject } from '@angular/core';
import { CheckoutService, Plan } from '../core/services/checkout.service';

@Component({
  selector: 'app-paywall-modal',
  standalone: true,
  template: `
    <div
      class="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/50 px-4 py-6 overflow-y-auto"
      (click)="onBackdrop($event)"
    >
      <div class="card w-full max-w-3xl p-6 sm:p-8 relative my-auto" (click)="$event.stopPropagation()">
        <button
          (click)="closed.emit()"
          class="absolute top-3 right-3 p-1.5 text-ink-500 hover:text-ink-900 rounded-md hover:bg-slate-100"
          aria-label="Fechar"
        >
          <svg viewBox="0 0 24 24" class="h-5 w-5" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round">
            <path d="M18 6L6 18M6 6l12 12" />
          </svg>
        </button>

        <h3 class="text-2xl sm:text-3xl font-extrabold text-center leading-tight">
          ✨ Gostou? Continue sem limites.
        </h3>
        <p class="text-center text-ink-700 mt-2 text-sm sm:text-base">
          ✅ +12.430 documentos resumidos esta semana<br>
          ✅ Pague com PIX em 1 toque
        </p>

        <!-- MVP: Só plano LIGHT avulso via Checkout Pro -->
        <div class="max-w-sm mx-auto mt-8">
          <div class="card p-6 sm:p-8 border-2 border-brand-600 shadow-lift text-center">
            <p class="text-sm font-bold text-brand-700 uppercase tracking-wide">Light</p>
            <p class="mt-3">
              <span class="text-5xl font-extrabold">R$9</span>
              <span class="text-ink-500 text-base"> avulso</span>
            </p>
            <p class="text-sm text-ink-600 mt-3 leading-relaxed">
              15 análises<br>
              Validade de 90 dias<br>
              PDFs até 100 páginas
            </p>
            <button (click)="onPurchase('LIGHT')" [disabled]="isProcessing()" class="btn-primary w-full mt-6 text-base py-3">
              @if (isProcessing()) {
                <svg class="animate-spin h-5 w-5 mx-auto" viewBox="0 0 24 24" fill="none">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                </svg>
              } @else {
                Comprar com PIX
              }
            </button>
          </div>
        </div>

        <!-- Planos PRO e POWER comentados — voltar após validar LIGHT
        <div class="grid sm:grid-cols-3 gap-4 mt-6">
          <div class="card p-5 border border-slate-200">
            <p class="text-xs font-bold text-ink-500 uppercase tracking-wide">Light</p>
            <p class="mt-2">
              <span class="text-3xl font-extrabold">R$9</span>
              <span class="text-ink-500 text-sm"> avulso</span>
            </p>
            <p class="text-xs text-ink-500 mt-1">15 análises · 90 dias</p>
            <button (click)="onPurchase('LIGHT')" [disabled]="isProcessing()" class="btn-ghost border border-slate-200 w-full mt-4 text-sm">
              @if (isProcessing()) {
                <svg class="animate-spin h-4 w-4" viewBox="0 0 24 24" fill="none">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                </svg>
              } @else {
                Comprar com PIX
              }
            </button>
          </div>

          <div class="card p-5 relative ring-2 ring-brand-600 shadow-lift">
            <span class="absolute -top-3 left-1/2 -translate-x-1/2 bg-brand-600 text-white text-[10px] font-bold px-2.5 py-1 rounded-full whitespace-nowrap">
              MAIS POPULAR
            </span>
            <p class="text-xs font-bold text-brand-700 uppercase tracking-wide">Pro</p>
            <p class="mt-2">
              <span class="text-3xl font-extrabold">R$19</span>
              <span class="text-ink-500 text-sm"> /mês</span>
            </p>
            <p class="text-xs text-ink-500 mt-1">80 análises · até 100 págs</p>
            <button (click)="onPurchase('PRO')" [disabled]="isProcessing()" class="btn-primary w-full mt-4 text-sm">
              @if (isProcessing()) {
                <svg class="animate-spin h-4 w-4" viewBox="0 0 24 24" fill="none">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                </svg>
              } @else {
                Assinar agora
              }
            </button>
          </div>

          <div class="card p-5 border border-slate-200">
            <p class="text-xs font-bold text-ink-500 uppercase tracking-wide">Power</p>
            <p class="mt-2">
              <span class="text-3xl font-extrabold">R$39</span>
              <span class="text-ink-500 text-sm"> /mês</span>
            </p>
            <p class="text-xs text-ink-500 mt-1">300 análises · até 200 págs</p>
            <button (click)="onPurchase('POWER')" [disabled]="isProcessing()" class="btn-ghost border border-slate-200 w-full mt-4 text-sm">
              @if (isProcessing()) {
                <svg class="animate-spin h-4 w-4" viewBox="0 0 24 24" fill="none">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                </svg>
              } @else {
                Assinar agora
              }
            </button>
          </div>
        </div>
        -->

        <p class="text-xs text-ink-500 mt-6 text-center">
          🔒 Pagamento seguro via Mercado Pago
        </p>
      </div>
    </div>
  `,
})
export class PaywallModalComponent {
  private readonly checkoutService = inject(CheckoutService);

  @Input({ required: true }) email!: string;
  @Output() closed = new EventEmitter<void>();

  readonly isProcessing = signal(false);

  onPurchase(plan: Plan): void {
    if (!this.email || this.isProcessing()) return;
    this.isProcessing.set(true);

    this.checkoutService.createCheckout(this.email, plan).subscribe({
      next: (response) => {
        // Redirecionar para Mercado Pago
        window.location.href = response.initPoint;
      },
      error: (err) => {
        this.isProcessing.set(false);
        const msg = err.error?.detail || err.error?.message || 'Erro ao criar checkout. Tente novamente.';
        alert(msg);
      },
    });
  }

  onBackdrop(event: MouseEvent): void {
    if (event.target === event.currentTarget) this.closed.emit();
  }
}
