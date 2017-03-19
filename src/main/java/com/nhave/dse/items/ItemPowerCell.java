package com.nhave.dse.items;

import java.util.List;

import com.nhave.dse.DeepSeaExpansion;
import com.nhave.dse.api.items.IPowerItem;
import com.nhave.dse.util.PowerUtils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPowerCell extends ItemBase implements IPowerItem
{
	int maxPower;
	public boolean isCreative = false;
	private EnumRarity rarity = EnumRarity.COMMON;
	
	public ItemPowerCell(String name, int power)
	{
		super(name);
		this.setMaxStackSize(1);
		this.setCreativeTab(DeepSeaExpansion.CREATIVETABTOOLS);
		this.maxPower = power;
		if (maxPower <= 0) this.isCreative = true;
	}
	
	public ItemPowerCell(String name, int power, EnumRarity rarity)
	{
		this(name, power);
		this.rarity = rarity;
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tabb, NonNullList<ItemStack> list)
	{
		ItemStack noPower = new ItemStack(this);
		list.add(noPower);
		if (!this.isCreative)
		{
			ItemStack maxPower = new ItemStack(this);
			setPower(maxPower, getMaxPower(maxPower));
			list.add(maxPower);
		}
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return this.rarity;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
	{
		//list.add(I18n.translateToLocal("tooltip.dse.charge") + ": " + getPowerInfo(stack));
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return !this.isCreative;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return Math.max(1.0 - (double)getPower(stack) / (double)getMaxPower(stack), 0);
	}

	@Override
	public int getPower(ItemStack itemStack)
	{
		if (this.isCreative) return 0;
		if(itemStack.getTagCompound() == null)
		{
			return 0;
		}
	
		int powerStored = Math.min(itemStack.getTagCompound().getInteger("power"), this.maxPower);
	
		return powerStored;
	}

	@Override
	public void setPower(ItemStack itemStack, int amount)
	{
		if (this.isCreative) return;
		if(itemStack.getTagCompound() == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}
	
		int powerStored = Math.max(Math.min(amount, getMaxPower(itemStack)), 0);
		itemStack.getTagCompound().setDouble("power", powerStored);
	}

	@Override
	public int getMaxPower(ItemStack itemStack)
	{
		return this.maxPower;
	}

	@Override
	public String getPowerInfo(ItemStack itemStack)
	{
		return (this.isCreative ? "∞RF" : (PowerUtils.getEnergyDisplay(getPower(itemStack)) + " / " + PowerUtils.getEnergyDisplay(getMaxPower(itemStack))));
	}
}