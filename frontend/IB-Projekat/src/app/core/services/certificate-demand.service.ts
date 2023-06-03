import { HttpClient } from '@angular/common/http';
import { EnvironmentInjector, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PaginatedResponse } from '../models/paginated-response.model';
import { CertificateDemandResponse } from '../models/certificate-demand-response.model';
import { CertificateDemandRequest } from '../models/certificate-demand-request.model';
import { environment } from 'src/environment/environment';

@Injectable({
  providedIn: 'root'
})
export class CertificateDemandService {

  constructor(
    private httpClient: HttpClient
  ) { }

  public create(certificateDemandRequest: CertificateDemandRequest): Observable<CertificateDemandResponse> {
    return this.httpClient.post<CertificateDemandResponse>(`${environment.baseUrl}/certificate-demand`, certificateDemandRequest);
  }

  public getByRequesterId(requesterId: number, page: number, size: number): Observable<PaginatedResponse<CertificateDemandResponse>> {
    return this.httpClient.get<PaginatedResponse<CertificateDemandResponse>>(`${environment.baseUrl}/certificate-demand/by-requester/${requesterId}?page=${page}&size=${size}`)
  }

  public getByRequesterIdPending(requesterId: number, page: number, size: number): Observable<PaginatedResponse<CertificateDemandResponse>> {
    return this.httpClient.get<PaginatedResponse<CertificateDemandResponse>>(`${environment.baseUrl}/certificate-demand/by-requester/pending/${requesterId}?page=${page}&size=${size}`)
  }

  public reject(id: number): Observable<CertificateDemandResponse> {
    return this.httpClient.put<CertificateDemandResponse>(`${environment.baseUrl}/certificate-demand/${id}/reject`, {});
  }

  public accept(id:number):Observable<CertificateDemandResponse>{
    return this.httpClient.put<CertificateDemandResponse>(`${environment.baseUrl}/certificate-demand/${id}/accept`, {});
  }

  public getAll(page: number, size: number): Observable<PaginatedResponse<CertificateDemandResponse>>{
    return this.httpClient.get<PaginatedResponse<CertificateDemandResponse>>(`${environment.baseUrl}/certificate-demand?page=${page}&size=${size}`)
    
  }
}
