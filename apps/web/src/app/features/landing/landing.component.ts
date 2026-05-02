import { Component, signal, inject } from '@angular/core';
import { AnalysisService, AnalysisResponse } from '../../core/services/analysis.service';
import { GateService } from '../../core/services/gate.service';
import { EmailGateModalComponent } from '../../shared/email-gate-modal.component';
import { PaywallModalComponent } from '../../shared/paywall-modal.component';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [EmailGateModalComponent, PaywallModalComponent],
  templateUrl: './landing.component.html',
})
export class LandingComponent {
  private readonly analysisService = inject(AnalysisService);
  readonly gate = inject(GateService);

  readonly fileName = signal<string | null>(null);
  readonly fileSize = signal<string | null>(null);
  readonly isDragging = signal(false);
  readonly result = signal<AnalysisResponse | null>(null);

  readonly showEmailGate = signal(false);
  readonly showPaywall = signal(false);
  readonly copied = signal(false);

  private currentFile: File | null = null;
  private pendingType: 'SUMMARIZE' | 'EXTRACT_KEY_POINTS' = 'SUMMARIZE';

  get isProcessing() {
    return this.analysisService.isProcessing();
  }

  get error() {
    return this.analysisService.error();
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.isDragging.set(true);
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    this.isDragging.set(false);
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.isDragging.set(false);
    const file = event.dataTransfer?.files?.[0];
    if (file) this.handleFile(file);
  }

  onSelect(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (file) this.handleFile(file);
  }

  analyze(type: 'SUMMARIZE' | 'EXTRACT_KEY_POINTS' = 'SUMMARIZE'): void {
    if (!this.currentFile) return;
    this.pendingType = type;

    const action = this.gate.decideNextAction();
    if (action === 'SHOW_EMAIL_GATE') {
      this.showEmailGate.set(true);
      return;
    }
    if (action === 'SHOW_PAYWALL') {
      this.showPaywall.set(true);
      return;
    }
    this.runAnalysis();
  }

  onEmailConfirmed(): void {
    this.showEmailGate.set(false);
    this.runAnalysis();
  }

  onEmailGateClosed(): void {
    this.showEmailGate.set(false);
  }

  onPaywallClosed(): void {
    this.showPaywall.set(false);
  }

  reset(): void {
    this.fileName.set(null);
    this.fileSize.set(null);
    this.currentFile = null;
    this.result.set(null);
    this.copied.set(false);
    this.analysisService.reset();
  }

  copy(): void {
    const text = this.result()?.result;
    if (!text) return;
    navigator.clipboard.writeText(text).then(
      () => {
        this.copied.set(true);
        setTimeout(() => this.copied.set(false), 2000);
      },
      () => {
        // Fallback caso clipboard API falhe (browser antigo / contexto não seguro)
        const ta = document.createElement('textarea');
        ta.value = text;
        document.body.appendChild(ta);
        ta.select();
        try { document.execCommand('copy'); this.copied.set(true); setTimeout(() => this.copied.set(false), 2000); } catch {}
        document.body.removeChild(ta);
      }
    );
  }

  private runAnalysis(): void {
    if (!this.currentFile) return;
    this.analysisService.analyze(this.currentFile, this.pendingType).subscribe({
      next: (response) => {
        this.result.set(response);
        this.gate.registerAnalysisCompleted();
      },
      error: (err) => {
        console.error('Erro ao analisar:', err);
      },
    });
  }

  private handleFile(file: File): void {
    this.currentFile = file;
    this.fileName.set(file.name);
    this.fileSize.set(this.formatSize(file.size));
    this.result.set(null);
    this.analysisService.reset();
  }

  private formatSize(bytes: number): string {
    if (bytes < 1024) return `${bytes} B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
  }
}
