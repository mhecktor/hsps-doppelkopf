import {Router, Request, Response, NextFunction} from 'express';
const cards = require('../assets/cards.json');

export class CardsRouter {
  router: Router

  /**
   * Initialize the HeroRouter
   */
  constructor() {
    this.router = Router();
    this.init();
  }

  /**
   * GET all Cards.
   */
  public getAll(req: Request, res: Response, next: NextFunction) {
    res.send(cards);
  }

  /**
   * Take each handler, and attach to one of the Express.Router's
   * endpoints.
   */
  init() {
    this.router.get('/', this.getAll);
  }

}

// Create the CardsRouter, and export its configured Express.Router
const cardsRoutes = new CardsRouter();
cardsRoutes.init();

export default cardsRoutes.router;
