package brass;

class BrassComputerPlayerAction
{
	private int[] action_info;
	private boolean action_selected;
	
	public BrassComputerPlayerAction()
	{
		action_selected = false;
		action_info = new int[6];
	}
	
	public boolean isActionSelected()
	{
		return action_selected;
	}
	
	public void displayContents()
	{
		System.out.println("inside this command are");
		System.out.println(action_info[0]);
		System.out.println(action_info[1]);
		System.out.println(action_info[2]);
		System.out.println(action_info[3]);
		System.out.println(action_info[4]);
	}
	
	public void selectBuildAction(int card_index, int city_id, int industry_id, int coal_city_id, int iron_city_id)
	{
		if (!action_selected)
		{
			action_info[0] = card_index;
			action_info[1] = BrassActionEnum.BUILD.getValue();
			action_info[2] = city_id;
			action_info[3] = industry_id;
			action_info[4] = coal_city_id;
			action_info[5] = iron_city_id;
			
			action_selected = true;
		}
	}
	
	public void selectLinkAction(int card_index, int link_id)
	{
		if (!action_selected)
		{
			action_info[0] = card_index;
			action_info[1] = BrassActionEnum.LINK.getValue();
			action_info[2] = link_id;
			
			action_selected = true;
		}
	}
	
	public void selectDoubleLinkAction(int card_index, int link_id, int link_id_2)
	{
		if (!action_selected)
		{
			action_info[0] = card_index;
			action_info[1] = BrassActionEnum.DOUBLE_RAIL.getValue();
			action_info[2] = link_id;
			action_info[3] = link_id_2;
			action_selected = true;
		}
	}
	
	public void selectTechUpgradeAction(int card_index, int tech_upgrade_1, int tech_upgrade_2)
	{
		if (!action_selected)
		{
			action_info[0] = card_index;
			action_info[1] = BrassActionEnum.UPGRADE.getValue();
			action_info[2] = tech_upgrade_1;
			action_info[3] = tech_upgrade_2;

			action_selected = true;
		}
	}
	
	public void selectDiscardAction(int card_index)
	{
		if (!action_selected)
		{
			action_info[0] = card_index;
			action_info[1] = BrassActionEnum.DISCARD.getValue();

			action_selected = true;
		}
	}
	
	public void selectTakeLoanAction(int card_index, int loan_id)
	{
		if (!action_selected)
		{
			action_info[0] = card_index;
			action_info[1] = loan_id;

			action_selected = true;
		}
	}
	
	public void selectSellCottonAction(int card_index)
	{
		if (!action_selected)
		{
			action_info[0] = card_index;
			action_info[1] = BrassActionEnum.SELL.getValue();

			action_selected = true;
		}
	}
	
	public int getActionID()
	{
		if (isActionSelected())
		{
			return action_info[1];
		}
		return -1;
	}
	
	public int getCardIndex()
	{
		if (isActionSelected())
		{
			return action_info[0];
		}
		return -1;
	}
	
	public int getLinkID()
	{
		if (isLinkAction())
		{
			return action_info[2];
		}
		return -1;
	}
	
	public int getCityID()
	{
		if (isBuildAction())
		{
			return action_info[2];
		}
		return -1;
	}
	
	public int getIndustryID()
	{
		if (isBuildAction())
		{
			return action_info[3];
		}
		return -1;
	}
	
	public int getCoalCityID()
	{
		if (isBuildAction())
		{
			return action_info[4];
		}
		return -1;
	}
	
	public int getIronCityID()
	{
		if (isBuildAction())
		{
			return action_info[5];
		}
		return -1;
	}
	
	public int getLoanAmount()
	{
		if (isTakeLoanAction())
		{
			return action_info[1];
		}
		return -1;
	}
	
	public int getFirstTechUpgrade()
	{
		if (isTechUpgradeAction())
		{
			return action_info[2];
		}
		return -1;
	}
	
	public int getSecondTechUpgrade()
	{
		if (isTechUpgradeAction())
		{
			return action_info[3];
		}
		return -1;
	}
	
	public boolean isBuildAction()
	{
		if (action_selected && action_info[1] == BrassActionEnum.BUILD.getValue())
		{
			return true;
		}
		return false;
	}
	
	public boolean isLinkAction()
	{
		if (action_selected && action_info[1] == BrassActionEnum.LINK.getValue())
		{
			return true;
		}
		return false;
	}
	
	public boolean isSellCottonAction()
	{
		if (action_selected && action_info[1] == BrassActionEnum.SELL.getValue())
		{
			return true;
		}
		return false;
	}
	
	public boolean isTechUpgradeAction()
	{
		if (action_selected && action_info[1] == BrassActionEnum.UPGRADE.getValue())
		{
			return true;
		}
		return false;
	}
	
	public boolean isDiscardAction()
	{
		if (action_selected && action_info[1] == BrassActionEnum.DISCARD.getValue())
		{
			return true;
		}
		return false;
	}
	
	public boolean isTakeLoanAction()
	{
		if (action_selected && action_info[1] >= BrassActionEnum.LOAN_10.getValue() && action_info[1] <= BrassActionEnum.LOAN_30.getValue())
		{
			return true;
		}
		return false;
	}
}
