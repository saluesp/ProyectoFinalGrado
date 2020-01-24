import { Injectable } from '@angular/core';
import { Persona } from './persona';
import {formatDate,DatePipe} from '@angular/common';
import { of, Observable, throwError } from 'rxjs';
import {HttpClient, HttpHeaders, HttpRequest, HttpEvent} from '@angular/common/http';
import {map, catchError, tap} from 'rxjs/operators';
import Swal from 'sweetalert2';
import {Router} from '@angular/router';


@Injectable()
export class PersonaService {
  private urlEndPoint: string = 'http://localhost:8080/api/persona';
  private httpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' })

  constructor(private http: HttpClient, private router: Router) { }

  getPersonas(page: number): Observable<any> {
  return this.http.get<Persona[]>(this.urlEndPoint + '/page/' + page).pipe(
    tap( (response:any) => {
      console.log('PersonaService: tap 1');
        (response.content as Persona[]).forEach( persona => {
        console.log(persona.nombre);
      });
    }),
    map( (response:any) => {
      (response.content as Persona[]).map(persona => {
      persona.nombre = persona.nombre.toUpperCase();
      let datePipe = new DatePipe('es');
      //persona.createAt = datePipe.transform(persona.createAt, 'dd-MM-yyyy');
      return persona;
    });
    return response;
    }),
    tap(response => {
      console.log('PersonaService: tap 2');
      (response.content as Persona[]).forEach(persona => {
        console.log(persona.nombre);
      }

      );
    })
  );
  }

  create(persona: Persona) : Observable<Persona> {
    return this.http.post(this.urlEndPoint, persona, {headers: this.httpHeaders}).pipe(
      map((response : any) => response.persona as Persona),
      catchError(e =>{
        if(e.status==400){
          return throwError(e);
        }
        console.error(e.error.mensaje);
        Swal.fire('Error al crear una persona', e.error.mensaje, 'error');
        return throwError(e);
      })
    )
  }


  getPersona(id): Observable<Persona>{
    return this.http.get<Persona>(`${this.urlEndPoint}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/persona']);
        Swal.fire('Error al editar',e.error.mensaje, 'error')
        return throwError(e)
      })
    )
  }

  update(persona: Persona):Observable <any>{
    return this.http.put<any>(`${this.urlEndPoint}/${persona.id}`, persona, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        if (e.status == 400) {
          return throwError(e);
        }
        console.error(e.error.mensaje);
        Swal.fire('Error al editar la persona', e.error.mensaje, 'error');
        return throwError(e);
      })
    )
  }

  delete(id: number): Observable<Persona>{
    return this.http.delete<Persona>(`${this.urlEndPoint}/${id}`, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        console.error(e.error.mensaje);
        Swal.fire('Error al eliminar la persona', e.error.mensaje, 'error');
        return throwError(e);
      })
    )
  }
  subirFoto(archivo: File, id): Observable<HttpEvent<{}>>{
    let formData= new FormData();
    formData.append("archivo",archivo);
    formData.append("id",id);
    const req = new HttpRequest('POST', `${this.urlEndPoint}/upload`, formData,{
      reportProgress: true
    });
    return this.http.request(req);
  }
}
