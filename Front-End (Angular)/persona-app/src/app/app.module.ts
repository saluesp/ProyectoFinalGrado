import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';

import { AppComponent } from './app.component';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import { DirectivaComponent } from './directiva/directiva.component';
import { PersonaComponent } from './persona/persona.component';
import { FormComponent } from './persona/form.component';
import { PaginatorComponent } from './paginator/paginator.component';
import { PersonaService } from './persona/persona.service';
import { RouterModule, Routes} from '@angular/router';
import { HttpClientModule} from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { registerLocaleData } from '@angular/common';
import localeES from '@angular/common/locales/es';
import { DetalleComponent } from './persona/detalle/detalle.component';
registerLocaleData(localeES, 'es');


const routes: Routes = [
  { path: "", redirectTo: "/persona", pathMatch: "full" },
  { path: "directivas", component: DirectivaComponent },
  { path: "persona", component: PersonaComponent },
  { path: "persona/page/:page", component: PersonaComponent },
  { path: "persona/form", component: FormComponent },
  { path: "persona/form/:id", component: FormComponent },
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    DirectivaComponent,
    PersonaComponent,
    FormComponent,
    PaginatorComponent,
    DetalleComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(routes),
  ],
  providers: [PersonaService, {provide: LOCALE_ID, useValue: 'es'}],
  bootstrap: [AppComponent]
})
export class AppModule { }
