import * as mocha from 'mocha';
import * as chai from 'chai';
import chaiHttp = require('chai-http');

import app from '../src/App';

chai.use(chaiHttp);
const expect = chai.expect;

describe('GET api/v1/cards', () => {

  it('responds with JSON array', () => {
    return chai.request(app).get('/api/v1/cards')
      .then(res => {
        expect(res.status).to.equal(200);
        expect(res).to.be.json;
        expect(res.body).to.be.an('object');
      });
  });

  it('should include Wolverine', () => {
    return chai.request(app).get('/api/v1/cards')
      .then(res => {
        let cardOne = res.body['1'];
        expect(cardOne).to.exist;
        expect(cardOne).to.have.all.keys([
          'color',
          'value',
          'image'
        ]);
      });
  });

});