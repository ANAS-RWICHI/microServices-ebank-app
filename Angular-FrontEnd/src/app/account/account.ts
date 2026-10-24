import { Component, inject } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AsyncPipe, DatePipe} from '@angular/common';

export interface Account {
  id: string;
  balance: number;
  createdAt: Date;
  type: string;
  customerId: string;
}
@Component({
  selector: 'app-account',
  imports: [
    AsyncPipe,
    DatePipe
  ],
  templateUrl: './account.html',
  styleUrl: './account.css',
})



export class Account {
 private http =inject(HttpClient);

  account$ = this.http.get<Account[]>('http://localhost:9999/EBANK-SERVICE/accounts');
}
