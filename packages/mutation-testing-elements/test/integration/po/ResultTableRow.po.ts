import { PageObject } from './PageObject.po';
import { By } from 'selenium-webdriver';
import { ProgressBar } from './ProgressBar.po';

export class ResultTableRow extends PageObject {

  private readonly nameTableElement = this.$.bind(this, 'td:nth-child(2)');
  public navigate = () => this.nameTableElement().findElement(By.css('a')).click();
  public name = () => this.nameTableElement().getText();
  public progressBar = () => new ProgressBar(this.$('td:nth-child(3)>div.progress'));
  public mutationScore = () => this.$('th:nth-child(4)').getText();
  public killed = () => this.$('td:nth-child(5)').getText();
  public survived = () => this.$('td:nth-child(6)').getText();
  public timeout = () => this.$('td:nth-child(7)').getText();
  public noCoverage = () => this.$('td:nth-child(8)').getText();
  public runtimeErrors = () => this.$('td:nth-child(9)').getText();
  public compileErrors = () => this.$('td:nth-child(10)').getText();
  public totalDetected = () => this.$('th:nth-child(11)').getText();
  public totalUndetected = () => this.$('th:nth-child(12)').getText();
  public totalMutants = () => this.$('th:nth-child(13)').getText();
}