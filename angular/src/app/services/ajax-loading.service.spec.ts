import { TestBed } from '@angular/core/testing';

import { AjaxLoadingService } from './ajax-loading.service';

describe('AjaxLoadingService', () => {
  let service: AjaxLoadingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AjaxLoadingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
