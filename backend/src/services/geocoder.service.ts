import {inject, Provider} from '@loopback/core';
import {getService} from '@loopback/service-proxy';
import {GeocoderDataSource} from '../datasources';

export interface GeoPoint {
  // y coordinate
  lat: number;
  // x coordinate
  lng: number;
}

export interface Geocoder {
  geocode(address: string): Promise<GeoPoint[]>;
}

export class GeocoderProvider implements Provider<Geocoder> {
  constructor(
    // geocoder must match the name property in the datasource json file
    @inject('datasources.geocoder')
    protected dataSource: GeocoderDataSource = new GeocoderDataSource(),
  ) {}

  value(): Promise<Geocoder> {
    return getService(this.dataSource);
  }
}
