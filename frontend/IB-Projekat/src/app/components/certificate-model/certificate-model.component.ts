import { AfterViewInit, Component, ElementRef, EventEmitter, Input, Output, Renderer2, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { CertificateResponse } from 'src/app/core/models/certificate-response.model';

@Component({
  selector: 'app-certificate-model',
  templateUrl: './certificate-model.component.html',
  styleUrls: ['./certificate-model.component.css']
})
export class CertificateModelComponent implements AfterViewInit {

  @Input() certificate: CertificateResponse = {} as CertificateResponse;

  @Output() emitter: EventEmitter<CertificateResponse> = new EventEmitter();

  @ViewChild('certificateModel') certificateModel: ElementRef;

  constructor(
    private renderer: Renderer2,
  ) {}

  ngAfterViewInit(): void {
    if (this.certificate.isPulled) {
      this.renderer.setStyle(
        this.certificateModel.nativeElement,
        'background-color',
        'rgb(235, 142, 142)'
      );
    }
  }

  allDataVisible: boolean = false;
  showDataLabel: string = "Show more..."

  changeDataVisibility() {
    this.allDataVisible = !this.allDataVisible;
    if (this.allDataVisible) {
      this.showDataLabel = "Show less..."
    } else {
      this.showDataLabel = "Show more..."
    }
  }

  // triggers when the user clicks on the certificate (cilcking the 'Show more...' button is no longer treated as clicking on the certificate)
  onClick() {
    this.emitter.emit(this.certificate);
  }

}
