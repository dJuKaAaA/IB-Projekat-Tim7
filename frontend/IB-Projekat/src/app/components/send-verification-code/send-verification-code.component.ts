import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-send-verification-code',
  templateUrl: './send-verification-code.component.html',
  styleUrls: ['./send-verification-code.component.css'],
})
export class SendVerificationCodeComponent {
  formGroup: FormGroup = new FormGroup({
    code: new FormControl(''),
  });
  @Output() sendCodeEvent = new EventEmitter();

  constructor(private authService: AuthService, private router: Router) {}

  public sendVerificationCode() {
    this.sendCodeEvent.emit(this.formGroup.value.code);
  }
}
