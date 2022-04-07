import {inject, lifeCycleObserver, LifeCycleObserver} from '@loopback/core';
import {juggler} from '@loopback/repository';

const config = {
  name: 'geocoder',
  connector: 'rest',
  options: {
    headers: {
      accept: 'application/json',
      'content-type': 'application/json',
    },
  },
  operations: [
    {
      template: {
        method: 'GET',
        url: 'https://maps.googleapis.com/maps/api/geocode/json',
        query: {
          address: '{address}',
          key: 'AIzaSyBqbaGO0CeLvfzhYyaBCAVFzYSfDWl6xJQ',
        },
        responsePath: '$.results[*].geometry.location',
      },
      functions: {
        toCoordinates: ['address'],
      },
    },
    {
      template: {
        method: 'GET',
        url: 'https://maps.googleapis.com/maps/api/geocode/json',
        query: {
          latlng: '{lat},{lng}',
          key: 'AIzaSyBqbaGO0CeLvfzhYyaBCAVFzYSfDWl6xJQ',
        },
        responsePath: '$.results[*].formatted_address',
      },
      functions: {
        toAddress: ['lat', 'lng'],
      },
    },
  ],
};

// Observe application's life cycle to disconnect the datasource when
// application is stopped. This allows the application to be shut down
// gracefully. The `stop()` method is inherited from `juggler.DataSource`.
// Learn more at https://loopback.io/doc/en/lb4/Life-cycle.html
@lifeCycleObserver('datasource')
export class GeocoderDataSource extends juggler.DataSource
  implements LifeCycleObserver {
  static dataSourceName = 'geocoder';
  static readonly defaultConfig = config;

  constructor(
    @inject('datasources.config.geocoder', {optional: true})
    dsConfig: object = config,
  ) {
    super(dsConfig);
  }
}
