import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './products.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductsDetail = (props: IProductsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productsDetailsHeading">
          <Translate contentKey="perfumeStoreManagerApp.products.detail.title">Products</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productsEntity.id}</dd>
          <dt>
            <span id="productID">
              <Translate contentKey="perfumeStoreManagerApp.products.productID">Product ID</Translate>
            </span>
          </dt>
          <dd>{productsEntity.productID}</dd>
          <dt>
            <span id="productName">
              <Translate contentKey="perfumeStoreManagerApp.products.productName">Product Name</Translate>
            </span>
          </dt>
          <dd>{productsEntity.productName}</dd>
          <dt>
            <span id="quantityAvailable">
              <Translate contentKey="perfumeStoreManagerApp.products.quantityAvailable">Quantity Available</Translate>
            </span>
          </dt>
          <dd>{productsEntity.quantityAvailable}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="perfumeStoreManagerApp.products.price">Price</Translate>
            </span>
          </dt>
          <dd>{productsEntity.price}</dd>
          <dt>
            <span id="dateImport">
              <Translate contentKey="perfumeStoreManagerApp.products.dateImport">Date Import</Translate>
            </span>
          </dt>
          <dd>
            {productsEntity.dateImport ? <TextFormat value={productsEntity.dateImport} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="expireDate">
              <Translate contentKey="perfumeStoreManagerApp.products.expireDate">Expire Date</Translate>
            </span>
          </dt>
          <dd>
            {productsEntity.expireDate ? <TextFormat value={productsEntity.expireDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="perfumeStoreManagerApp.products.description">Description</Translate>
            </span>
          </dt>
          <dd>{productsEntity.description}</dd>
          <dt>
            <span id="categoryID">
              <Translate contentKey="perfumeStoreManagerApp.products.categoryID">Category ID</Translate>
            </span>
          </dt>
          <dd>{productsEntity.categoryID}</dd>
          <dt>
            <span id="volume">
              <Translate contentKey="perfumeStoreManagerApp.products.volume">Volume</Translate>
            </span>
          </dt>
          <dd>{productsEntity.volume}</dd>
          <dt>
            <Translate contentKey="perfumeStoreManagerApp.products.categories">Categories</Translate>
          </dt>
          <dd>{productsEntity.categories ? productsEntity.categories.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/products" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/products/${productsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ products }: IRootState) => ({
  productsEntity: products.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductsDetail);
