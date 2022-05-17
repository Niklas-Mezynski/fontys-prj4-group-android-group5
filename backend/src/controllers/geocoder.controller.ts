import { authenticate } from '@loopback/authentication';
import { inject } from '@loopback/core';
import { get, HttpErrors, param } from '@loopback/rest';
import { Geocoder } from '../services';

@authenticate('jwt')
export class GeocoderController {
  constructor(
    @inject('services.Geocoder') protected geoService: Geocoder,
  ) { }

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
    const response = await this.geoService.toCoordinates(address);
    const json = JSON.stringify(response[0]);
    return json;
  }

  @get('/toAddress/{latitude},{longitude}', {
    responses: {
      '200': {
        description: 'Get address of coordinates',
        // content: {'application/json': {schema: GeoSchema}},
      },
    },
  })
  async toAddress(
    @param.path.number('latitude') lat: number,
    @param.path.number('longitude') lng: number,
  ): Promise<any> {
    const response = await this.geoService.toAddress(lat, lng);
    const jsonString = JSON.stringify(response[0]);
    let json = JSON.parse(jsonString);
    return json;
  }

  @get('/toCity/{latitude},{longitude}', {
    responses: {
      '200': {
        description: 'Get address of coordinates',
        // content: {'application/json': {schema: GeoSchema}},
      },
    },
  })
  async toCity(
    @param.path.number('latitude') lat: number,
    @param.path.number('longitude') lng: number,
  ): Promise<any> {
    const response = await this.geoService.toAddress(lat, lng);
    const jsonString = JSON.stringify(response[0]);
    let json = JSON.parse(jsonString);
    let returnValue;
    json.forEach((element: any) => {
      if (element.types.includes("locality")) {
        returnValue = element;
        return;
      }
    })
    if (!returnValue) {
      throw new HttpErrors.NotFound("There is no city name for these coordinates.");
    }
    return returnValue;
  }

  @get('/toAddressShort/{latitude},{longitude}', {
    responses: {
      '200': {
        description: 'Get address of coordinates',
        // content: {'application/json': {schema: GeoSchema}},
      },
    },
  })
  async toAddressShort(
    @param.path.number('latitude') lat: number,
    @param.path.number('longitude') lng: number,
  ): Promise<any> {
    const response = await this.geoService.toAddressShort(lat, lng);
    const jsonString = JSON.stringify(response[0]);
    let json = JSON.parse(jsonString);
    return {
      "address": json
    }
  }
}
