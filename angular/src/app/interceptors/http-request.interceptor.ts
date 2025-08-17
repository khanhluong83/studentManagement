import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AjaxLoadingService } from '../services/ajax-loading.service';
import { finalize } from 'rxjs';

export const httpRequestInterceptor: HttpInterceptorFn = (req, next) => {
  
  const ajaxLoadingService = inject(AjaxLoadingService);

  // Turn on the loading spinner
  ajaxLoadingService.loadingOn();

  return next(req).pipe(
    finalize(() => {
      // Turn off the loading spinner
      ajaxLoadingService.loadingOff();
    })
  );
};
