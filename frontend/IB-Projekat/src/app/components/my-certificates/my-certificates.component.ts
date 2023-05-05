import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { CertificateResponse } from 'src/app/core/models/certificate-response.model';
import { PaginatedResponse } from 'src/app/core/models/paginated-response.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { CertificateService } from 'src/app/core/services/certificate.service';

@Component({
  selector: 'app-my-certificates',
  templateUrl: './my-certificates.component.html',
  styleUrls: ['./my-certificates.component.css']
})
export class MyCertificatesComponent {

  myCertificates: Array<CertificateResponse> = [];

  constructor(
    private certificateService: CertificateService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.certificateService.getForUser(this.authService.getId(), 0, 10).subscribe((response: PaginatedResponse<CertificateResponse>) => {
      this.myCertificates = response.content;
    })
  }

  pullCertificate(certificate: CertificateResponse) {
    if (certificate.isPulled) {
      alert("This certificate is already pulled!");
      return;
    }
    if (confirm(`Are you sure you want to pull the certificate with serial number: "${certificate.serialNumber}"?`)) {
      this.certificateService.pull(certificate.serialNumber).subscribe({
        next: () => {
          alert("Certificate, and the ones in it's downward chain, have been pulled successfully!")
        }, error: (error) => {
          if (error instanceof HttpErrorResponse) {
            alert(error.error.message);
          }
        }
      })
    }
  }
}
