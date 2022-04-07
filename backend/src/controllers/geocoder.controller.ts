import {inject} from '@loopback/core';
import {get, param} from '@loopback/rest';
import {Geocoder} from '../services';

export class GeocoderController {
  constructor(
    @inject('services.Geocoder') protected geoService: Geocoder,
  ) {}

  @get('/toCoordinates/{address}', {
    responses: {
      '200': {
        description: 'Get coordinates of address',
        // content: {'application/json': {schema: GeoSchema}},
      },
    },
  })
  async toCoordinates(
    @param.path.string('address') address: string,
  ): Promise<string> {
    const response = await this.geoService.geocode(address);
    let json = JSON.stringify(response[0]);
    return json;
  }
}
