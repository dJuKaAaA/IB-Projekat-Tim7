export interface CertificateDemandRequest {
    type: string,
    requestedSigningCertificateId: number,
    requesterId: number,
    reason: string
}